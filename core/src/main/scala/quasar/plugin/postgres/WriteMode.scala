/*
 * Copyright 2014–2019 SlamData Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quasar.plugin.postgres

import slamdata.Predef.{Eq => _, _}

import argonaut._, Argonaut._

import cats._

sealed trait WriteMode extends Product with Serializable

object WriteMode {
  case object Create extends WriteMode
  case object Replace extends WriteMode

  implicit val codecJson: CodecJson[WriteMode] =
    CodecJson(
      _ match {
        case Create => jString("create")
        case Replace => jString("replace")
      },
      c => c.as[String] flatMap {
        case "create" => DecodeResult.ok(Create)
        case "replace" => DecodeResult.ok(Replace)
        case _ => DecodeResult.fail("Valid write modes are 'create' and 'replace'", c.history)
      })

  implicit val eqv: Eq[WriteMode] =
    Eq.fromUniversalEquals
}
