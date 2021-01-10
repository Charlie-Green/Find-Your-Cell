package by.zenkevich_churun.findcell.result.ui.shared.cps

import by.zenkevich_churun.findcell.entity.entity.CoPrisoner


/** Defines options (their number, a label and a callback for each)
  * for [CoPrisoner]s within a list. **/
interface CoPrisonerOptionsAdapter {

    /** Label resource for the first option button.
      * @param relation the value of [CoPrisoner.relation] property
      *        for the [CoPrisoner] the options are requested for. **/
    fun label1(relation: CoPrisoner.Relation): Int

    /** Same as [label1], but for the second option.
      * If only 1 option is supported, the method must return 0. **/
    fun label2(relation: CoPrisoner.Relation): Int

    /** Invoked when the first option is selected.
      * @param relation the value of [CoPrisoner.relation] property
      *        for the [CoPrisoner] the options are requested for.
      * @param position adapter position of the item whose option is selected. **/
    fun onSelected1(relation: CoPrisoner.Relation, position: Int)

    /** Same as [onSelected1]. If [label2] is 0, this method is never invoked. **/
    fun onSelected2(relation: CoPrisoner.Relation, position: Int) {
        throw NotImplementedError(
            "Either label2 must return 0 or this method must be implemented."
        )
    }
}