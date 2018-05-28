# Yelp-Data-Set-Analysis
## Objective

To solve the below queries using Spark SQL 2.3.0 .

## Queries

1. Summarize the number of reviews by US city, by business category.<br>

2. Rank all cities by # of stars descending, for each category.<br>

3. What is the average rank (# stars) for businesses within 10 miles of the University of Wisconsin - Madison, by type of       business?<br>
Center: University of Wisconsin - Madison<br>
Latitude: 43 04’ 30” N, Longitude: 89 25’ 2” W<br>
Decimal Degrees: Latitude: 43.0766, Longitude: -89.4125<br>
The bounding box for this problem is ~10 miles, which we will loosely define as 10 minutes. So the bounding box is a square box, 20 minutes long each side (of longitude and latitude), with UWM at the center.<br>

4. Rank reviewers by number of reviews. For the top 10 reviewers, show their average number of stars, by category.<br>

5. For the top 10 and bottom 10 food business near UWM (in terms of stars), summarize star rating for reviews in January through May.<br>

## Dataset
https://www.yelp.com/dataset_challenge

## Solution
Used inbuilt Spark SQL JSON parser. Also, I have used Scala-IDE extension of eclipse to build the project. I have used the local deployment mode of Spark.<br>
**Note:** Just to save time you can save the tables in the cache or using persist and save a lot of time executing the scripts
using shell. Otherwise if you will use the below mentioned way you will see the data loading when running each solution as 
the data is being loaded temporarily. 

## Steps to Run : 
1. Import project and build the project. <br>
2. Build the project using the command
  `` 
  mvn package
  ``
3. Run the following command to run the respective programs <br>

###### Question-1

``spark-submit --class com.sparkScala.YelpDataSetSparkSQL.Question1 $project_folder/target/YelpDataSetSparkSQL-0.0.1-SNAPSHOT.jar /path/to/business.json <output Path>``

###### Question-2
``spark-submit --class com.sparkScala.YelpDataSetSparkSQL.Question2 $project_folder/target/YelpDataSetSparkSQL-0.0.1-SNAPSHOT.jar /path/to/business.json <output Path>``

###### Question-3
``spark-submit --class com.sparkScala.YelpDataSetSparkSQL.Question3 $project_folder/target/YelpDataSetSparkSQL-0.0.1-SNAPSHOT.jar /path/to/business.json <output Path>``

###### Question-4
``spark-submit --class com.sparkScala.YelpDataSetSparkSQL.Question4 $project_folder/target/YelpDataSetSparkSQL-0.0.1-SNAPSHOT.jar /path/to/business.json /path/to/review.json /path/to/user.json <output Path>``

###### Question-5
``spark-submit --class com.sparkScala.YelpDataSetSparkSQL.Question5 $project_folder/target/YelpDataSetSparkSQL-0.0.1-SNAPSHOT.jar /path/to/business.json /path/to/review.json <output Path>``
