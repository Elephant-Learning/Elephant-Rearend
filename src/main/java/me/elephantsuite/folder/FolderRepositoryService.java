package me.elephantsuite.folder;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.folder.controller.FolderRequest;
import me.elephantsuite.response.Response;
import me.elephantsuite.user.ElephantUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class FolderRepositoryService {

	private final FolderRepository repository;


	public Folder save(Folder folder) {
		return repository.save(folder);
	}

	public Folder getFolderById(long folderId) {
		if (repository.existsById(folderId)) {
			return repository.getReferenceById(folderId);
		}

		return null;
	}
}
