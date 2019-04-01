package com.mj.common.model

case class Coordinate(latitude: Double, longitude: Double)

case class Bike(id: Long,
                name: String,
                address: String,
                latitude: Option[Double],
                longitude: Option[Double],
                coordinates: Option[Coordinate],
                position: Option[String])
