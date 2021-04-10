package com.example

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{Formats, FullTypeHints}

case class WINEMAG_ROW(id: Option[Long], country: Option[String], points: Option[Int], price: Option[Float], title: Option[String], variety: Option[String], winery: Option[String])

object JsonReader extends App {
  val appName: String = "Json-reader RDD"
  val spark = SparkSession.builder()
    .appName(appName)
    .config("spark.driver.memory", "5g")
    .master("local")
    .getOrCreate()

  val distFile: RDD[String] = spark.sparkContext.textFile(args(0))
  implicit val formats: Formats = Serialization.formats(FullTypeHints(List(classOf[WINEMAG_ROW])))
  distFile.map(x => parse(x).extract[WINEMAG_ROW])
      .foreach {x => println(x.toString)}
}
