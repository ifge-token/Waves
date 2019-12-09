package com.wavesplatform.common

import java.util.Base64
import java.util.concurrent.TimeUnit

import com.wavesplatform.common.ZkSnarkBenchmark.{CurveSt, Groth16St, MerkleSt}
import com.wavesplatform.lang.v1.EnvironmentFunctionsBenchmark.{curve25519, randomBytes}
import com.wavesplatform.zwaves.bls12.{Groth16, PedersenMerkleTree}
import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole
import scorex.crypto.signatures.{Curve25519, Signature}

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Array(Mode.AverageTime))
@Threads(1)
@Fork(1)
@Warmup(iterations = 10)
@Measurement(iterations = 20)
class ZkSnarkBenchmark {
  @Benchmark
  def groth16(st: Groth16St, bh: Blackhole): Unit =
    bh.consume(Groth16.verify(st.vk, st.proof, st.inputs))

  // for comparison
  @Benchmark
  def sigVerify(st: CurveSt, bh: Blackhole): Unit =
    bh.consume(Curve25519.verify(Signature @@ st.signature, st.message, st.publicKey))

  @Benchmark
  def merkleTest(st: MerkleSt, bh: Blackhole): Unit =
    bh.consume(PedersenMerkleTree.addItem(st.root0, st.proof0, st.index, st.elements1))
}

object ZkSnarkBenchmark {

  @State(Scope.Benchmark)
  class Groth16St {
    val vk: Array[Byte] = Base64.getDecoder.decode("s6Fw3I8k6rDlE1BJcs833aBONDLHtvx2AWD92/r2qWWc05SGxm+beILW+G2KrB4QpmeojJCTEDqiFAZ4tCkLGppmaiBNRunPepzNtXY+1TPiqRsDdpHP86U7lgng2OAEADUDAFAVbNLRMNzYHbDf7LRppGh2xiQILqHz0OqLU9dcS9vuNQVuVYIQqcUGoObYkPiXh4gEjeojZ/ZHYzBgRMzlqI2cyNPR1uFYHYBgrUtOcOoIueQqyvgii9NynelqEJHSYXFkhefEbegIXinjA9lHKkFuhkHRPW1lqIU7uMofmLTOEON7XyjTZf7HvJ0IoNU368JBygoz97xgjmTGe2R+F2M+tQjnA0qSNV4ve9/RyOmUZLIbvHPnC+HUCFnwGFuJF0LLkNL+LizhD+nRa6uBFgOGNKJo88XwRIjAC+KZOCP3nrxgegS4m/bRxmG6o6a03AlETTySuenJqp79DS7pTiBLmmKi0qCnOMyeiC5N25n4wKkCPDqUxeDfYBlDlRiGpRh8Lt/jHyJAAMdaUQK/bbla1wfBBSkq+WIqgzQRpFAOlP20GgbLlYitIeEpigMdNI7yna6gF/H/yj5AyoyctmX0JaRGs0JMbJXH5LSQjrnds41/8O/EoJzIVvGJt8MBfhtjM8XqRymtkjvo0c7N5PHw3mcVcJqQ5+GMytQ/IhIi7SrIqlesrpbWkG0koDcKMhIZM/EqXWQQApIp2B0w0LyJOjeRe3vg6R08QOJmc/2OiquIX2+3wo9wgmwzk6XX2gc8LF8qWr4m7Kk6qt2OIk2tLZK+2FR7l1+AkGEJ9rAh3KZ01rmTRRQk7BdXkNtxldeVfqs5CH7Tik8jGPEzpq06Aqh56GeG8+JZ+0MQpnidx2WwcNP/RwNQ2K0eiWrcvf2b8Zwq7fan2EmPIckcsQ4TDtcUYlZ/jtv8oQ8AbYVbjxCsb2+ANMbsiGfKojIKcDUqtiWCKA0A15oYvJ1+ypYRFgVFV4W9J0hTDNOAULv4Ir5pjtESEnbipEYilmSIuVIxoxBAQfGdYLfn7ktLcwpTBglFWQD40MGpY52ZWhOuQdGAhb2hiYHY8LLaqEpQKPlE6XjDbMkF32NoyNWLaJaankwoP0dKhxPec1cUp8DmzBDEzA/r7ct6e1TkkjFUNVdbmrPjaH4oywuOYrBjJl4LqS6sn0YtDMfXnDMxbj9hHjyiCvvJzCZoQF4Usz+nxwys9J/ltRjeGofKgQoYD8c6vyib5Zep3swXIP96yRJ4EY9VLx4ZHrKiXmbBkoOsZdZuOScTRqxr2eWXlRZsydm//A7HROZx0LYll/UbASK4RIz5biDG2Z1AIg2bjfCCXX069KgUsnTsrwVlx3XhF/FFje42YP447PvcnEx8vWaXYMIo9nABOOKdZlOipw8mq+/bn1H53vUUYxmGghiJ+cCSNMPLrX8DLKYOL9x5dDcpt2MJWZ7mjQ+lTtUFoNvV8lzQXncyobLubjPaKeGlLA2vPRnmqQSYNZqp+/J6Z7dtIt1btoW30jt0OM8D")
    val proof: Array[Byte] = Base64.getDecoder.decode("tKrzmvZK5lnOj3xbe/3x1Yu8aqPxMnPOawFPM0JWDBH+WDFKBUwlToXaefpY7gkxg14eUONY5rJI3dj6abI7b6gZtgyVP7Xr1HXtMpDX9i5xs4kgXxCcCZ+1ZNox311LEJLQoctW/Qi042T0t1FQ9ZSWUlyZkSEb/l8fC8akq2oEDIFICs+PmuzGdgDuuHndlV09I+bY5hgNhRV1UvteD3W8m7Q0vGO/W/milDXu6u65gch97W2Wwwjj0Ags+j5l")
    val inputs: Array[Byte] = Base64.getDecoder.decode("GCddXmFUIRlXriGVQ1A3t6cwg0w4lmWXGqI+X3mMbmNiO51k5CvrWHd5GOwVb5eb5Imcjj55Q6oKQptRSZqe9TY//vzQ+VNG5bkLiCpTgphYwzXH/GFowK7AjCvy8YhQbFQ1V8sPbpjH8rYlxveMdHW41maMYoW6sJuHpoz1RnNkAyGKRQ0sRUD1n/ohaE/LVLnB4F3cN3cea3MdTmPgPGIWCE5a6oeQRGI/RpKH6kY2BMeuta4jrizVRrcIeFvtYu+j4v31CSmSxiCTHx2SqF/QcSJGnkkdSj9eIZkiO+VjqFi39hRbsfsMYCDUZ8P8QhrYWb+6cFErJ/1PLPjOKFkgBG5pxLo5ifZGjTL5kbtBD6kKritHOHRuANqarO40IiS1yffpaJVrXyJDy0rn2K79XQhyO85tWfK6oT1Q4GwYgxGI9rYyfuKaqrYUbIWPoDCwc9lmSYF9klmjn7LyNDymCC9mmzCiTz4ggzlrR/6DhmJGoDon3wZ5N0jhuCvBPog54HohNv84S1zZLtzEbmlRawFa/q1sl/h5Fl+9KQYyN3SWGN0YIOd3eqtu80UBL8YkOZs8BL6Fgb6KXGULo1VkAHR9q+RSurwCvMqpk/nPzFyZ4MviHxSAmSOUNN2cXzYONrxHZYXGRid1/YxUSukQ57OtwTXsUVIeYG/HTUI=")
  }

  @State(Scope.Benchmark)
  class MerkleSt {
    val root0: Array[Byte] = Base64.getDecoder.decode("YK6g9RXHtw3vE3zmjCGCdLqrij2BwvPCO0E8Bm977ks=")
    val proof0: Array[Byte] = Base64.getDecoder.decode("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")
    val index = 23
    val elements1: Array[Byte] = Base64.getDecoder.decode("GrfbyxFPhfTsam3aY8yqeY072ZrT3DTO8SrSRg4nJZ9nF1OpFPuvXQlcbsqFrGUkgUlokbCealGEw0J8G/H2oWFOY3CHDDhqBQGzUzk+/R3uYljv0YS/Wnb41IKeDSzfDcrJ41FdxBXgptFlh+TM3OKlgJ2jSAF9mqE3v7dUD/1n6conMUMPB+yeP5fapHBGu2OtlDmiHjzuGG6xrrW7tW45mNToh8yTb+POgZP+IvCmf7b8Tzzs0Z9fv998Q5HdXwrH6ts1cC9GBn9GwlWiDxfbKHKE+XS86tNoGPje5MFE7fWtv5XKzkGirbKRuKsBrLDrwl4UwaruqMwk1jJ4jTnsYzLpaGW7nZ46g+Mx5THu8481Kl7zWTFyRXVLIlvCc3eI3oqsjglbnCZ/7xgG4mnlDWqKYBuWzkmm6pCXB0JV7p+/1G7KcT7SIYoWUdf/XPedxh3N3Qgp3xhyh2VpcAMiK9JrMi8j4EQEsxE9Qm58z8aZ+fGBDkEJPaX0TJ1mDQvC4jQH6Sw96KjRLwezomn2Y5rtXcsxqX5UEhuYk39cxPBUX1tC5ap9qn0IPJaQ1Aj7tZvKhm4H1z3zAdwILTzjXYFOE2NyGZub0DY0g16XZKzhOCrtyeSauxm2pRzjNOFKLyEnk8/8a4bAXfODxGA4tCoBlv8ixFGa1agonvVPMc9FP6/S/26YEL491LGZsnqQ5AvvPt1oe/TmcfK/P1AxgsO2chZPqwddisQNRinb8x5NE8ptEWh9He7uutE/LeAGQVN5BYNpEMijP1RDj1FQkDFgglvvh6rgqm7sZr4lh/8wInwzJMMVTN9Dw1uvRHI5Igir1gcvs0vbuqhAf1NRcY5tnD6PxSTl79M1SkTpg1MSmpYJcISdoFNmfq0LVRUyp47f6gjWt+qVkyJAe3xp0GaEmaMKvvj2xVxLdxRRlRRTYABMFjU63LBttYjn0e1qkAvv1JveIrreATBq5Q==")
  }

  @State(Scope.Benchmark)
  class CurveSt {
    val (privateKey, publicKey) = curve25519.generateKeypair
    val message                 = randomBytes(150 * 1024)
    val signature               = curve25519.sign(privateKey, message)
  }
}



