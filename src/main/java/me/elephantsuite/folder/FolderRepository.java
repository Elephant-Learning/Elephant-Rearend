package me.elephantsuite.folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM folder_deck_ids WHERE folder_deck_ids.deck_ids = ?1", nativeQuery = true)
    int deleteDeckFromFolder(long deckId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM folder_deck_ids WHERE folder_deck_ids.folder_id = ?1", nativeQuery = true)
    int deleteFolderFromDeckIds(long folderId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Folder f WHERE f.id = ?1")
    int deleteFolder(long folderId);
}
