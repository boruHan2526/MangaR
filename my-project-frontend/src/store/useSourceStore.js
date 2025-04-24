// src/store/useSourceStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSourceStore = defineStore('source', () => {
  // —— 1. 初始值快照
  const INITIAL_IS_ALT = false
  const INITIAL_DISABLE_ANIM = false

  // —— 2. 状态
  const isAltSource = ref(INITIAL_IS_ALT)
  const disableAnimation = ref(INITIAL_DISABLE_ANIM)

  // —— 3. 行为：数据源开关
  const toggleSource = () => {
    isAltSource.value = !isAltSource.value
  }
  const setSource = (value) => {
    isAltSource.value = value
  }

  // —— 4. 行为：动画开关
  const disableAnim = () => {
    disableAnimation.value = true
  }
  const enableAnimation = () => {
    disableAnimation.value = false
  }
  const setAnimation = (value) => {
    disableAnimation.value = value
  }

  // —— 5. 手动重置所有状态
  function resetAll() {
    isAltSource.value = INITIAL_IS_ALT
    disableAnimation.value = INITIAL_DISABLE_ANIM
  }

  return {
    // 数据源
    isAltSource,
    toggleSource,
    setSource,
    // 动画
    disableAnimation,
    disableAnim,
    enableAnimation,
    setAnimation,
    // 重置
    resetAll,
  }
})
