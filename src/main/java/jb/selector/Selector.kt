package jb.selector

import jb.config.Opts
import jb.data.ScoreTuple
import jb.data.SelectedTuple

interface Selector {

    fun select(validationResults: ScoreTuple, opts: Opts): SelectedTuple

}
