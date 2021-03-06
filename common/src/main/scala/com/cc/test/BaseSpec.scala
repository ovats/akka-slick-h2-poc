package com.cc.test

import com.cc.view.RestView
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

trait BaseSpec extends AnyFlatSpecLike with Matchers with BeforeAndAfterEach with ScalaFutures with RestView {}
