package com.materialsouk.meetmyshow.Repository


import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.models.MoviesItem

class MovieRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("movies")

    fun addMovie(movie: MoviesItem, onComplete: (Boolean) -> Unit) {
        collection.document(movie.movieId)
            .set(movie)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getAllMovies(onResult: (List<MoviesItem>) -> Unit) {
        collection.get()
            .addOnSuccessListener { snapshot ->
                val movieList = snapshot.documents.mapNotNull {
                    it.toObject(MoviesItem::class.java)
                }
                onResult(movieList)
            }
    }

    fun updateMovie(movie: MoviesItem, onComplete: (Boolean) -> Unit) {
        collection.document(movie.movieId)
            .set(movie)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteMovie(movieId: String, onComplete: (Boolean) -> Unit) {
        collection.document(movieId)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
