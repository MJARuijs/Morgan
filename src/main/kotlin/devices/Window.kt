package devices

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.system.MemoryUtil.NULL

class Window(title: String) {

    private var identifier = 0L

    var running = true
        private set

    var resized = true
        private set

    var width = 960
        private set

    var height = 540
        private set

    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()

    val keyboard = Keyboard()

    val mouse = Mouse()

    init {

        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw RuntimeException("Unable to initialize GLFW")
        }

        val monitor = glfwGetPrimaryMonitor()
        val video = glfwGetVideoMode(monitor)

        if (video != null) {

            width = video.width() / 2
            height = video.height() / 2

            if (width > height) {
                width = height * 16 / 9
            } else {
                height = width * 9 / 16
            }
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        identifier = glfwCreateWindow(width, height, title, NULL, NULL)
        if (identifier == NULL) {
            throw RuntimeException("Failed to create GLFW window")
        }

        glfwSetWindowCloseCallback(identifier) {
            running = false
        }

        glfwSetWindowSizeCallback(identifier) { _, newWidth: Int, newHeight: Int ->
            resized = (newWidth != width) || (newHeight != height)
            width = newWidth
            height = newHeight
        }

        glfwSetKeyCallback(identifier) { _, keyInt: Int, _, actionInt: Int, _ ->

            val key = Key.fromInt(keyInt)
            val action = Action.fromInt(actionInt)

            if (key != null && action != null) {
                keyboard.post(key, action)
            }
        }

        glfwSetMouseButtonCallback(identifier) { _, buttonInt: Int, actionInt: Int, _ ->

            val button = Button.fromInt(buttonInt)
            val action = Action.fromInt(actionInt)

            if (button != null && action != null) {
                mouse.post(button, action)
            }
        }

        glfwSetCursorPosCallback(identifier) { _, x: Double, y: Double ->
            mouse.moved = (mouse.x != x) || (mouse.y != y)
            mouse.dx = x - mouse.x
            mouse.dy = y - mouse.y
            mouse.x = x
            mouse.y = y
        }

        glfwMakeContextCurrent(identifier)
        glfwSwapInterval(1)
        createCapabilities()
    }

    fun synchronize() {
        glfwSwapBuffers(identifier)
    }

    fun poll() {
        resized = false
        mouse.moved = false
        glfwPollEvents()
        keyboard.poll()
        mouse.poll()
    }

    fun capture() {
        glfwSetInputMode(identifier, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        mouse.captured = true
    }

    fun release() {
        glfwSetInputMode(identifier, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
        mouse.captured = false
    }

    fun destroy() {

        glfwDestroyWindow(identifier)
        identifier = 0L

        glfwTerminate()
    }
}
