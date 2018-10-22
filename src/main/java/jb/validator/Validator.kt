package jb.validator

import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.ScoreTuple
import jb.data.ValidatingTestingTuple

interface Validator {

    fun validate(clfs: List<Model>, validatingTestingTuple: ValidatingTestingTuple, opts: Opts): ScoreTuple

}
