package com.vishnu.springmongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vishnu.springmongo.documents.Commit;

public interface CommitRepository extends MongoRepository<Commit, String>  {
	
	List<Commit> findByRevisionNumber(String revisionNumber);
	
	//@Query ("{revisionNumber : ?0}")
	//List<Commit> findByRevNum (String revNum);
}
