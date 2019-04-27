package com.sun.health.newwork.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.DeleteResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleMongoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testShowCollections() {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        for (String collectionName : collectionNames) {
            System.out.println(collectionName);
        }
    }

    @Test
    public void testFindOne() {
        Criteria id = Criteria.where("id").is(1);
        User user = mongoTemplate.findOne(new Query(id), User.class, "user");
        System.out.println(user);
    }

    @Test
    public void testQuery() {
//        MongoCollection<Document> userCollection = mongoTemplate.getCollection("user");
        Criteria criteria = Criteria.where("id").lt(20);
        Query query = new Query(criteria);
        List<User> users = mongoTemplate.find(query, User.class, "user");
        for (User user : users) {
            System.out.println(user);
        }
    }

//    @Test
//    public void testCollection() {
//        MongoCollection<Document> collection = mongoTemplate.getCollection("user");
//        FindIterable<User> users = collection.find(BsonDocument.parse("{'id': 1}"), User.class);
//        User first = users.first();
//        System.out.println(first);
//    }

    @Test
    public void testSortAndLimit() {
        Query query = new Query(Criteria.where("id").gt(0));
//        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "id"))); // 过时
        query.with(new Sort(Sort.Direction.DESC, "id"));
        query.skip(10).limit(5);
        List<User> users = mongoTemplate.find(query, User.class, "user");
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testCount() {
        long count = mongoTemplate.count(new Query(new Criteria()), User.class);
        System.out.println(count);
    }

    @Test
    public void testAggregate() {
        Criteria criteria = Criteria.where("id").gt(0);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("grade").count().as("grade_amount"));
        AggregationResults<HashMap> result = mongoTemplate.aggregate(aggregation, "user", HashMap.class);
        System.out.println(result);
    }

    @Test
    public void testSave() {
        mongoTemplate.insert(new User(10001, "孙健", 11));
        User one = mongoTemplate.findOne(new Query(Criteria.where("name").is("孙健")), User.class, "user");
        System.out.println(one);
    }

    @Test
    public void testUpdate() {
        User before = mongoTemplate.findOne(new Query(Criteria.where("id").is(10001)), User.class, "user");
        System.out.println(before);
//        before.setName("孙勇");
        mongoTemplate.updateMulti(new Query(Criteria.where("name").is("孙健")), new Update().set("name", "孙勇"), "user");
        User after = mongoTemplate.findOne(new Query(Criteria.where("id").is(10001)), User.class, "user");
        System.out.println(after);
    }

    @Test
    public void testRemove() {
        DeleteResult remove = mongoTemplate.remove(new Query(Criteria.where("id").is(10001)), User.class, "user");
        System.out.println(remove.getDeletedCount());
        System.out.println(remove.wasAcknowledged());
    }

}
