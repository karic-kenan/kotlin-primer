package oop

import kotlin.text.format
import kotlin.text.padStart

object SealedClassAndInterface {

    // Define our base sealed class for all media content
    sealed class MediaContent {
        abstract val title: String
        abstract val durationSeconds: Int

        // Common method to format duration as minutes:seconds
        fun formattedDuration(): String {
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
    }

    // Song subclass
    data class Song(
        override val title: String,
        override val durationSeconds: Int,
        val artist: String,
        val album: String,
        val genre: String
    ) : MediaContent() {
        // Song-specific method
        fun artistAndAlbum(): String = "$artist - $album"
    }

    // Podcast subclass
    data class Podcast(
        override val title: String,
        override val durationSeconds: Int,
        val host: String,
        val episode: Int,
        val showName: String
    ) : MediaContent() {
        // Podcast-specific method
        fun episodeInfo(): String = "Episode $episode of $showName"
    }

    // Audiobook subclass
    data class Audiobook(
        override val title: String,
        override val durationSeconds: Int,
        val author: String,
        val narrator: String,
        val chapters: List<String>
    ) : MediaContent() {
        // Audiobook-specific method
        fun chapterCount(): Int = chapters.size
    }

    // Function that demonstrates exhaustive when with sealed classes
    fun getMediaDescription(content: MediaContent): String {
        return when (content) {
            is Song -> "${content.title} by ${content.artist} from album ${content.album}"
            is Podcast -> "${content.title} - ${content.episodeInfo()}"
            is Audiobook -> "${content.title} by ${content.author}, narrated by ${content.narrator}"
            // No else branch needed! The compiler knows these are all possible cases
        }
    }

    // Define a sealed interface for media actions
    sealed interface MediaAction {
        // All implementing classes will have to provide this information
        val actionName: String
        val requiresNetwork: Boolean
    }

    // Play action
    object Play : MediaAction {
        override val actionName: String = "Play"
        override val requiresNetwork: Boolean = false

        fun execute(content: MediaContent) {
            println("Playing ${content.title} (${content.formattedDuration()})")
        }
    }

    // Download action
    data class Download(val quality: String) : MediaAction {
        override val actionName: String = "Download"
        override val requiresNetwork: Boolean = true

        fun execute(content: MediaContent) {
            println("Downloading ${content.title} at $quality quality")
        }
    }

    // Share action
    data class Share(val platform: String) : MediaAction {
        override val actionName: String = "Share"
        override val requiresNetwork: Boolean = true

        fun execute(content: MediaContent) {
            println("Sharing ${content.title} on $platform")
        }
    }

    // Nested sealed hierarchy for playback states
    sealed class PlaybackState {
        data class Playing(
            val content: MediaContent,
            val currentPositionSeconds: Int
        ) : PlaybackState() {
            fun remainingTime(): Int = content.durationSeconds - currentPositionSeconds
        }

        data class Paused(
            val content: MediaContent,
            val currentPositionSeconds: Int
        ) : PlaybackState() {
            fun percentComplete(): Double =
                (currentPositionSeconds.toDouble() / content.durationSeconds) * 100
        }

        object Stopped : PlaybackState()

        data class Loading(
            val content: MediaContent,
            val loadingProgress: Int // 0-100
        ) : PlaybackState()
    }

    // Function to handle playback states
    fun getPlaybackStatusText(state: PlaybackState): String {
        return when (state) {
            is PlaybackState.Playing -> {
                val remaining = state.remainingTime()
                "Playing: ${state.content.title} (${remaining / 60}:${remaining % 60} remaining)"
            }

            is PlaybackState.Paused -> {
                val percent = String.format("%.1f", state.percentComplete())
                "Paused: ${state.content.title} ($percent% complete)"
            }

            is PlaybackState.Stopped -> "Stopped"
            is PlaybackState.Loading -> "Loading ${state.content.title}: ${state.loadingProgress}%"
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        // Create media content instances
        val song = Song(
            title = "Believer",
            durationSeconds = 204,
            artist = "Imagine Dragons",
            album = "Evolve",
            genre = "Pop Rock"
        )

        val podcast = Podcast(
            title = "Modern Software Development",
            durationSeconds = 1800,
            host = "Jane Smith",
            episode = 42,
            showName = "Tech Talk Weekly"
        )

        val audiobook = Audiobook(
            title = "The Great Gatsby",
            durationSeconds = 9000,
            author = "F. Scott Fitzgerald",
            narrator = "Jake Gyllenhaal",
            chapters = listOf("Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4")
        )

        // Test the description function
        println("Media Descriptions:")
        println(getMediaDescription(song))
        println(getMediaDescription(podcast))
        println(getMediaDescription(audiobook))
        println()

        // Test media actions
        println("Media Actions:")
        Play.execute(song)
        Download("High").execute(podcast)
        Share("Twitter").execute(audiobook)
        println()

        // Test playback states
        println("Playback States:")
        val playingState = PlaybackState.Playing(song, 45)
        val pausedState = PlaybackState.Paused(podcast, 900)
        val stoppedState = PlaybackState.Stopped
        val loadingState = PlaybackState.Loading(audiobook, 75)

        println(getPlaybackStatusText(playingState))
        println(getPlaybackStatusText(pausedState))
        println(getPlaybackStatusText(stoppedState))
        println(getPlaybackStatusText(loadingState))
    }
}
