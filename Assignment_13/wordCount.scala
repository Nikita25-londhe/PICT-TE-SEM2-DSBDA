/*Commands
nc -lk 9999
./spark-shell -i path_to_your_scala_file
*/
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
val session=SparkSession.builder.appName("myapp").master("local[*]").getOrCreate()
import spark.implicits._
val lines=spark.readStream.format("socket").option("host","localhost").option("port",9999).load()
val words=lines.as[String].flatMap(_.split(" "))
val count=words.groupBy("value").count()
val query=count.writeStream.outputMode("complete").format("console").start()
query.awaitTermination()
