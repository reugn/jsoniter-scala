package com.github.plokhotnyuk.jsoniter_scala.benchmark

class MissingRequiredFieldsReadingSpec extends BenchmarkSpecBase {
  private val benchmark = new MissingRequiredFieldsReading
  
  "MissingRequiredFieldsReading" should {
    "return some parsing error" in {
      benchmark.readAVSystemGenCodec() shouldBe
        "Cannot read com.github.plokhotnyuk.jsoniter_scala.benchmark.MissingRequiredFields, field s is missing in decoded data"
      benchmark.readCirce() shouldBe
        "Attempt to decode value on failed cursor: DownField(s)"
      benchmark.readDslJsonScala() shouldBe
        "Mandatory properties (s, i) not found at position: 1, following: `{`, before: `}`"
      benchmark.readJacksonScala() shouldBe
        """Missing required creator property 's' (index 0)
          | at [Source: (byte[])"{}"; line: 1, column: 2]""".stripMargin
      benchmark.readJsoniterScala() shouldBe
        """missing required field "s", offset: 0x00000001, buf:
          |           +-------------------------------------------------+
          |           |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
          |+----------+-------------------------------------------------+------------------+
          || 00000000 | 7b 7d                                           | {}               |
          |+----------+-------------------------------------------------+------------------+""".stripMargin
      benchmark.readJsoniterScalaWithoutDump() shouldBe
        """missing required field "s", offset: 0x00000001"""
      benchmark.readJsoniterScalaWithStacktrace() shouldBe
        """missing required field "s", offset: 0x00000001, buf:
          |           +-------------------------------------------------+
          |           |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
          |+----------+-------------------------------------------------+------------------+
          || 00000000 | 7b 7d                                           | {}               |
          |+----------+-------------------------------------------------+------------------+""".stripMargin
      benchmark.readPlayJson() shouldBe
        "JsResultException(errors:List((/s,List(JsonValidationError(List(error.path.missing),WrappedArray()))), (/i,List(JsonValidationError(List(error.path.missing),WrappedArray())))))"
      benchmark.readSprayJson() shouldBe
        "Object is missing required member 's'"
      benchmark.readUPickle() shouldBe
        "missing keys in dictionary: s, i at index 1"
    }
  }
}