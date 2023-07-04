package com.adoyo.diaryapp.model

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberGalleryState(): GalleryState {
    return remember {
        GalleryState()
    }
}

class GalleryState {
    val images = mutableListOf<GalleryImage>()
    val imagesToBeDeleted = mutableListOf<GalleryImage>()


    fun addImages(galleryImage: GalleryImage) {
        images.add(galleryImage)
    }

    fun removeImage(galleryImage: GalleryImage) {
        images.remove(galleryImage)
        imagesToBeDeleted.add(galleryImage)
    }

    fun clearDeletedImages() {
        imagesToBeDeleted.clear()
    }


}

data class GalleryImage(
    val image: Uri,
    val remotePath: String = ""
)