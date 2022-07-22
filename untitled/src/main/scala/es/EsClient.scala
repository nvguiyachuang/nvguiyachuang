package es

import java.text.SimpleDateFormat
import java.util.Date

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core.{Index, Search, SearchResult}
import org.elasticsearch.index.query.{BoolQueryBuilder, MatchQueryBuilder, RangeQueryBuilder}
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.highlight.HighlightBuilder
import org.elasticsearch.search.sort.SortOrder

import scala.collection.mutable.ListBuffer

object EsClient {

  private var factory: JestClientFactory = _

  def getClient: JestClient = {
    if (factory == null) {
      factory = new JestClientFactory
      factory.setHttpClientConfig(new HttpClientConfig.Builder("http://hadoop102:9200")
        .multiThreaded(true)
        .maxTotalConnection(20)
        .connTimeout(10000).readTimeout(1000).build())
    }
    factory.getObject
  }

  def putIndex(): Unit = {
    val jest: JestClient = getClient
    val actorList = new java.util.ArrayList[String]()
    actorList.add("tom")
    actorList.add("Jack")
    val movieBean = MovieTest("102", "中途岛之战", actorList)
    val datestring: String = new SimpleDateFormat("yyyyMMdd").format(new Date)
    val index: Index = new Index.Builder(movieBean)
      .index("movie_test1021_" + datestring)
      .`type`("_doc")
      .id(movieBean.id)
      .build()

    jest.execute(index)
    jest.close()
  }

  def main(args: Array[String]): Unit = {
    val jest: JestClient = getClient
    var query = "{\n  \"query\": {\n    \"bool\": {\n      \"should\": [\n        {\"match\": {\n          \"name\": \"red sea\"\n        }}\n      ],\n      \"filter\": {\n         \"range\": {\n           \"doubanScore\": {\n             \"gte\": 3\n           }\n         }\n      }\n    } \n    \n  }\n  ,\n  \"sort\": [\n    {\n      \"doubanScore\": {\n        \"order\": \"asc\"\n      }\n    }\n  ],\n  \"from\": 0, \n  \"size\": 20, \n  \"highlight\": {\n    \"fields\": { \n      \"name\": {}\n    }\n  } \n}"

    val searchBuilder = new SearchSourceBuilder
    val boolbuilder = new BoolQueryBuilder()
    boolbuilder.should(new MatchQueryBuilder("name", "red sea"))
      .filter(new RangeQueryBuilder("doubanScore").gte(3))
    searchBuilder.query(boolbuilder)
    searchBuilder.sort("doubanScore", SortOrder.ASC)
    searchBuilder.from(0)
    searchBuilder.size(20)
    searchBuilder.highlight(new HighlightBuilder().field("name"))

    val query2: String = searchBuilder.toString
    println(query2)
    val search: Search = new Search.Builder(query2).addIndex("movie1021_index").addType("movie").build()
    val result: SearchResult = jest.execute(search)
    val hitList: java.util.List[SearchResult#Hit[java.util.Map[String, Any], Void]] = result.getHits(classOf[java.util.Map[String, Any]])
    val resultList: ListBuffer[java.util.Map[String, Any]] = new ListBuffer[java.util.Map[String, Any]]
    import scala.collection.JavaConversions._
    for (hit <- hitList) {
      val source: java.util.Map[String, Any] = hit.source
      resultList.add(source)
    }
    println(resultList.mkString("\n"))

    jest.close()
  }

  case class MovieTest(id: String, movie_name: String, actionNameList: java.util.List[String])

}
