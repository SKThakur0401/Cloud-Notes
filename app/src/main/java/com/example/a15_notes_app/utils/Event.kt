package com.example.a15_notes_app.utils


// We need this class so that we can access data which is wrapped inside object of this class ONLY ONCE!
// We want that data to be accessed only once... because there is a problem... when we create a "viewModel"
// which is shared b/w 2 fragments (e.g. NoteFragment & MainFragment in our case) and if the they both have
// "requireActivity()" context, then it means both are accessing the same object of the view model.... and now..
// If u change a liveData, (e.g. status live data) I changed it in Note fragment by pressing "save button"  and it shows a Toast
// "note Updated! " , now
// value of this live data has been changed... but when I go back to "Main fragment" ... there also if u have an observer
// on that live data... that observer will also be kicked!!! because that variable has been changed.. the function within observer
// of that variable in this fragment will also execute... and now if u move again to another note fragment or same note fragment...
// "note Updated!" toast will appear again, and will appear every time u enter a new fragment because that live data is triggered each
// time...

open class Event<out T>(private val content: T) {       // it is just like "Consumable" class in Commerce-app ,

    var hasBeenHandled = false
        private set // Allow external read but not write


    fun getContentIfNotHandled(): T? {          // By putting our data inside object of "Event" class.. we ensure that,
        return if (hasBeenHandled) {            // that data will only be accessed once... because we r gonna access it
            null                                // using this function only :)
        } else {
            hasBeenHandled = true
            content
        }
    }

    // Below function returns the content even if it was used earlier... (We don't have any use here though)
    fun peekContent(): T = content
}
