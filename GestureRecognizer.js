export default class GestureRecognizer extends EventTarget {
    constructor() {
        super()

        this.pointerDownsSeenDuringTapInterval = 0
        this.pointerUpsSeenDuringTapInterval = 0
        this.watchingForTap = false
    }

    watch(element) {
        element.addEventListener("pointerdown", this.handlePointerDown.bind(this))
        element.addEventListener("pointerup", this.handlePointerUp.bind(this))
    }

    handlePointerDown(e) {
        if (!this.watchingForTap) {
            setTimeout(this.handleTimerElapsed.bind(this), 500)
            this.watchingForTap = true
        }
        this.pointerDownsSeenDuringTapInterval++
    }

    handlePointerUp(e) {
        this.pointerUpsSeenDuringTapInterval++
        if (this.pointerDownsSeenDuringTapInterval == 3 &&
            this.pointerUpsSeenDuringTapInterval == 3) {
            this.dispatchEvent(new CustomEvent("threefingertap"))
        }
    }

    handleTimerElapsed() {
        this.pointerDownsSeenDuringTapInterval = 0
        this.pointerUpsSeenDuringTapInterval = 0
        this.watchingForTap = false
    }

}