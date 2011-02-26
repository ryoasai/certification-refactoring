package sample.repository.impl;

import java.io.File;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import sample.common.io.CharSeparatedFileRepository;
import sample.domain.Work;
import sample.domain.WorkKey;
import sample.repository.WorkRepository;

@Repository
public class WorkRepositoryImpl extends CharSeparatedFileRepository<WorkKey, Work> implements WorkRepository {

	@Inject
	public WorkRepositoryImpl(
			@Value("${work.master.file}") File masterFile, 
			@Value("${work.work.file}") File workFile) {

		setMasterFile(masterFile);
		setWorkFile(workFile);
		
		System.out.println(masterFile);
		System.out.println(workFile);
	}
}
