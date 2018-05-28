package com.sparkScala.YelpDataSetSparkSQL

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions


object Question1 {
  
  
  
  def main(args: Array[String]): Unit = {
    if(args.length!=2){
      System.err.println("Enter <inputPath to Business.json> <outputPath>")
      return -1;
    }
    
    
    val inputPath = args(0)
    val outputPath = args(1)
    val conf = new SparkConf().setMaster("local").setAppName("Question-1 using Spark SQL")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().appName("Yelp Data Set US cities review count").getOrCreate()
    import spark.implicits._
    val businessData = spark.read.json(inputPath)
    val explodedBusinessData = businessData.withColumn("Category",functions.explode($"categories"))
    explodedBusinessData.createOrReplaceTempView("ExplodedCategoriesBusinessData")
    val rslt = spark.sql("Select city, category, sum(review_count) as Review_Count from ExplodedCategoriesBusinessData "+ 
                          "Where latitude>=24.520833 and latitude<=49.384472 and longitude>=-124.7844079 and longitude<=-66.9513812 and "+ 
                          "state NOT LIKE (\"ON\") AND state NOT LIKE(\"QC\") Group By City, Category Order by City, Category")
    rslt.repartition(1).write.format("com.databricks.spark.csv").option("header",true).save(outputPath)
  }
}