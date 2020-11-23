package by.zenkevich_churun.findcell.prisoner.repo


/** Return type for [PrisonerRepository.save] method. **/
enum class SavePrisonerResult {
    SUCCESS,
    ERROR,
    IGNORED
}