package com.sparkScala.YelpDataSetSparkSQL

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions
import org.apache.spark.sql.expressions.UserDefinedFunction
import scala.collection.mutable.WrappedArray
import org.apache.spark.sql.catalyst.expressions.WindowSpec
import org.apache.spark.sql.expressions.WindowSpec
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Dataset
import java.sql.Date


object Question5 {
  
  
  
   def main(args: Array[String]): Unit = {
    if(args.length!=3){
      System.err.println("Enter <inputPath to Business.json> <inputPath to Review.json> <outputPath>")
      return -1;
    }
    
    
    val businessPath = args(0)
    val reviewPath = args(1)
    
    val outputPath = args(2)
    val conf = new SparkConf().setMaster("local").setAppName("Question-5 using Spark SQL")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().appName("Yelp Data Set-Top 10 and Bottom 10 Food Businesses near UW-Madison - Summary Star Rating").getOrCreate()
    import spark.implicits._
    
    val businessData = spark.read.json(businessPath).filter($"latitude">=42.909333 && $"latitude"<=43.243266 && $"longitude">=(-89.579166) && $"longitude">=(-89.24583))
    val explodedBusinessData = businessData.withColumn("Category",functions.explode($"categories")).filter($"Category".contains("Restaurants")||$"Category".contains("Food")||$"Category".contains("Burger")||$"Category".contains("Pizza")).groupBy(functions.col("business_id").toString()).agg($"business_id").join(businessData,"business_id")
    val topBusinesses = explodedBusinessData.withColumn("rank",functions.row_number().over(Window.orderBy(functions.col("stars").desc))).where($"rank"<=10)
    val bottomBusinesses = explodedBusinessData.withColumn("rank",functions.row_number().over(Window.orderBy(functions.col("stars")))).where($"rank"<=10)
    val topBottomBusinesses = topBusinesses.union(bottomBusinesses).select(functions.col("business_id"),functions.col("name"),functions.col("stars").as("business_stars"))
    
    val reviewData = spark.read.json(reviewPath)
    val usersReviews = reviewData.join(topBottomBusinesses,Seq("business_id")).select("business_id","name", "review_id", "date","stars").where((functions.col("date").substr(6,2))<=5 && functions.col("date").substr(6, 2)>=1)
    
    
    
    
    val rslt = usersReviews.orderBy(functions.col("business_id"), functions.col("stars").desc)
    
    
    rslt.repartition(1).write.format("com.databricks.spark.csv").option("header",true).save(outputPath)
  }
}  