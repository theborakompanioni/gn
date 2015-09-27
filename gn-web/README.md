
http://localhost:8080/api
```
{  
   "_links":{  
      "trending":{  
         "rel":"trending",
         "href":"http://localhost:8080/articles/popular"
      },
      "create":{  
         "rel":"create",
         "href":"http://localhost:8080/articles"
      },
      "login":{  
         "rel":"login",
         "href":"http://localhost:8080/auth/login"
      },
      "popular":{  
         "rel":"popular",
         "href":"http://localhost:8080/articles/popular"
      },
      "newest":{  
         "rel":"newest",
         "href":"http://localhost:8080/articles/newest"
      },
      "verification":{  
         "rel":"verification",
         "href":"http://localhost:8080/articles/verification"
      }
   },
   "resource":""
}
```

http://localhost:8080/articles?sort=createdAt,desc

http://localhost:8080/articles/verification?sort=createdAt,desc



