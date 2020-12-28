package by.zenkevich_churun.findcell.prisoner.repo.profile

import by.zenkevich_churun.findcell.entity.entity.Prisoner


/** Return type for [ProfileRepository.save] method. **/
sealed class SavePrisonerResult {
    object Idle: SavePrisonerResult()
    object Error: SavePrisonerResult()
    object NoInternet: SavePrisonerResult()

    class Success(
        /** Adapter positions for items that were automatically deleted
          * while save operation was running. This list respects change of item indices
          * next to a deleted item, that is, after an item at position N is deleted,
          * positions of all the following items are decreased by 1. **/
        val deletePositions: List<Int>,

        /** The "clear" [Prisoner] instance (the one that was saved,
          * after removing blank [Contact]s). **/
        val clearPrisoner: Prisoner
    ): SavePrisonerResult()
}