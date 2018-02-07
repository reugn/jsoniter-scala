package com.github.plokhotnyuk.jsoniter_scala.macros

import java.nio.charset.StandardCharsets._
import java.time.Duration

import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros.CirceEncodersDecoders._
//import com.github.plokhotnyuk.jsoniter_scala.macros.JacksonSerDesers._
import com.github.plokhotnyuk.jsoniter_scala.macros.JsoniterCodecs._
import io.circe.java8.time._
//import io.circe.parser._
import io.circe.syntax._
import org.openjdk.jmh.annotations.Benchmark
import play.api.libs.json.Json

class ArrayOfDurationsBenchmark extends CommonParams {
  val obj: Array[Duration] = (1 to 128).map { i =>
    val x = ((i * 1498724053) / Math.pow(10, i % 10)).toInt
    Duration.ofSeconds(x.toLong, x)
  }.toArray
  val jsonString: String = obj.mkString("[\"", "\",\"", "\"]")
  val jsonBytes: Array[Byte] = jsonString.getBytes
/* FIXME Circe doesn't parse properly "PT-4H-33M-0.00001638S"
  @Benchmark
  def readCirce(): Array[Duration] = decode[Array[Duration]](new String(jsonBytes, UTF_8)).fold(throw _, x => x)
*/
/* FIXME Jackson doesn't parse properly "PT-4H-33M-0.00001638S"
  @Benchmark
  def readJacksonScala(): Array[Duration] = jacksonMapper.readValue[Array[Duration]](jsonBytes)
*/
  @Benchmark
  def readJsoniterScala(): Array[Duration] = JsonReader.read(durationArrayCodec, jsonBytes)
/* FIXME Play-json doesn't parse properly "PT-4H-33M-0.00001638S"
  @Benchmark
  def readPlayJson(): Array[Duration] = Json.parse(jsonBytes).as[Array[Duration]]
*/
  @Benchmark
  def writeCirce(): Array[Byte] = printer.pretty(obj.asJson).getBytes(UTF_8)
/* FIXME Jackson serializes Duration as a number
  @Benchmark
  def writeJacksonScala(): Array[Byte] = jacksonMapper.writeValueAsBytes(obj)
*/
  @Benchmark
  def writeJsoniterScala(): Array[Byte] = JsonWriter.write(durationArrayCodec, obj)

  @Benchmark
  def writeJsoniterScalaPrealloc(): Int = JsonWriter.write(durationArrayCodec, obj, preallocatedBuf, 0)

  @Benchmark
  def writePlayJson(): Array[Byte] = Json.toBytes(Json.toJson(obj))
}