package me.elephantsuite.folder;

import lombok.AllArgsConstructor;
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

    public void deleteFolder(Folder folder) {
		repository.deleteFolderFromDeckIds(folder.getId());
		repository.deleteFolder(folder.getId());
    }
}
