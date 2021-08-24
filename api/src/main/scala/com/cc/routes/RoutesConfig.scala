package com.cc.routes

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Printer

trait RoutesConfig extends FailFastCirceSupport {
  implicit val customPrinter = Printer.spaces2.copy(dropNullValues = true)
}
