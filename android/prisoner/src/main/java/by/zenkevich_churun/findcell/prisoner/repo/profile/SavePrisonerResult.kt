package by.zenkevich_churun.findcell.prisoner.repo.profile


/** Return type for [ProfileRepository.save] method. **/
enum class SavePrisonerResult {
    IDLE,
    SUCCESS,
    ERROR,
    NO_INTERNET
}