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


object Question4 {
  
  
  
   def main(args: Array[String]): Unit = {
    if(args.length!=4){
      System.err.println("Enter <inputPath to Business.json> <inputPath to Review.json> <inputPath to Users.json> <outputPath>")
      return -1;
    }
    
    
    val businessPath = args(0)
    val reviewPath = args(1)
    val userPath = args(2)
    val outputPath = args(3)
    val conf = new SparkConf().setMaster("local").setAppName("Question-4 using Spark SQL")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().appName("Yelp Data Set Top 10 Users Avg Ratings category wise").getOrCreate()
    import spark.implicits._
    val userData = spark.read.json(userPath)
    val topUsers = userData.withColumn("rank",functions.row_number().over(Window.orderBy(functions.col("review_count").desc))).where($"rank"<=10)
    
    
    val reviewData = spark.read.json(reviewPath)
    
    val topUsersReviews = reviewData.join(topUsers,Seq("user_id")).select("user_id","name", "review_count", "business_id","rank")
    topUsersReviews.createOrReplaceTempView("topUsersReview")
    val businessData = spark.read.json(businessPath)
    val explodedBusinessData = businessData.withColumn("Category",functions.explode($"categories"))
    explodedBusinessData.createOrReplaceTempView("ExplodedCategoriesBusinessData")
    
    val rslt = spark.sql("SELECT rank, topUsersReview.user_id as User_id, topUsersReview.name as User_name, topUsersReview.review_count as Review_Count, ExplodedCategoriesBusinessData.category, AVG(ExplodedCategoriesBusinessData.stars) as Ratings "+
                         "FROM topUsersReview JOIN ExplodedCategoriesBusinessData ON topUsersReview.business_id=ExplodedCategoriesBusinessData.business_id "+
                         "GROUP BY rank, topUsersReview.user_id,User_name,topUsersReview.Review_Count, category ORDER BY topUsersReview.review_count DESC, Ratings DESC "                            
    
    )
    
    
    rslt.repartition(1).write.format("com.databricks.spark.csv").option("header",true).save(outputPath)
  }
}  