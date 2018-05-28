package com.sparkScala.YelpDataSetSparkSQL

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions


object Question2 {
   def main(args: Array[String]): Unit = {
    if(args.length!=2){
      System.err.println("Enter <inputPath to Business.json> <outputPath>")
      return -1;
    }
    
    
    val inputPath = args(0)
    val outputPath = args(1)
    val conf = new SparkConf().setMaster("local").setAppName("Question-2 using Spark SQL")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().appName("Yelp Data Set Rank by Avg Stars").getOrCreate()
    import spark.implicits._
    val businessData = spark.read.json(inputPath)
    val explodedBusinessData = businessData.withColumn("Category",functions.explode($"categories"))
    explodedBusinessData.createOrReplaceTempView("ExplodedCategoriesBusinessData")
    val rslt = spark.sql("Select city, category, AVG(stars) as Star_Rating, Row_Number() over (order by AVG(Stars) DESC) as rank_ordered_business  from ExplodedCategoriesBusinessData "+ 
                          "Group By City, Category Order by Star_Rating DESC")
    rslt.repartition(1).write.format("com.databricks.spark.csv").option("header",true).save(outputPath)
  }
}