<template>
  <div class="container" style="margin-top: 8px">
    <!-- 显示伤害值 -->
    <div
      v-if="showDamage"
      style="
        margin-top: 200px;
        margin-bottom: -150px;
        text-align: center;
        font-size: 50px;
        font-weight: bold;
        color: red;
      "
    >
      💥 总伤害: {{ totalDamage }}
    </div>
    <div class="row" style="margin-top: 0px">
      <div class="col-md-12" style="margin-top: 3px"></div>
    </div>
    <div
      style="
        height: 40px;
        border: 2px solid rgb(7, 120, 232);
        margin-top: 542px;
        margin-bottom: -502px;
      "
    >
      <button
        class="btn btn-primary"
        type="button"
        style="margin-left: 400px"
        @click="rollOneByOne"
        :disabled="oneByOneDisabled"
      >
        OneByOne
      </button>

      <button
        class="btn btn-primary"
        type="button"
        style="margin-left: 295px"
        @click="rollAllIn"
        :disabled="allInDisabled"
      >
        AllIn
      </button>
    </div>
    <div
      style="
        margin-top: 124px;
        min-height: 275px;
        border-style: solid;
        border-color: rgb(198, 26, 47);
        margin-bottom: -502px;
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 80px; /* 每个方块间距 */
      "
    >
      <div
        @click="fillR('die1')"
        style="
          width: 160px;
          height: 160px;
          background-color: lightgray;
          display: flex;
          align-items: center;
          justify-content: center;
          border: 2px solid black;
          font-size: 100px;
        "
      >
        {{ dice.die1 }}
      </div>

      <div
        @click="fillR('die2')"
        style="
          width: 160px;
          height: 160px;
          background-color: lightgray;
          display: flex;
          align-items: center;
          justify-content: center;
          border: 2px solid black;
          font-size: 100px;
        "
      >
        {{ dice.die2 }}
      </div>

      <div
        @click="fillR('die3')"
        style="
          width: 160px;
          height: 160px;
          background-color: lightgray;
          display: flex;
          align-items: center;
          justify-content: center;
          border: 2px solid black;
          font-size: 100px;
        "
      >
        {{ dice.die3 }}
      </div>
    </div>
    <div class="row" style="margin-top: 630px; height: 186px">
      <div class="col-md-4">
        <button
          class="btn btn-danger"
          type="button"
          @click="useSkillR"
          :disabled="!rAvailable"
        >
          R
        </button>
        <div style="margin-top: 6px; font-weight: bold">
          当前牌型：{{ rProgress.join(" ") }} / [1, 6, 1, 5, 1, 1, 2, 3]
        </div>
        <div style="color: #dc3545; font-weight: bold; font-size: 14px">
          🧊 未攻击回合：{{ noAttackTurns }} / 3
        </div>
      </div>
      <div class="col-md-4">
        <img
          src="/images/gameRcharacter.png"
          alt=""
          style="
            margin-left: 100px;
            width: 200px;
            height: 200px;
            object-fit: contain;
          "
        />
      </div>
      <div class="col-md-4">
        <div style="margin-bottom: 30px">
          <button
            class="btn btn-primary"
            type="button"
            style="margin: 0px"
            @click="useSkillQ"
            :disabled="!(oneByOneDisabled && allInDisabled)"
          >
            Q
          </button>
        </div>
        <div style="margin-bottom: 30px">
          <button
            class="btn btn-primary"
            type="button"
            @click="useSkillW"
            :class="{ 'gold-glow': wEnabled }"
            :disabled="!wEnabled"
          >
            W
          </button>
        </div>
        <div>
          <button
            class="btn btn-primary"
            type="button"
            @click="useSkillE"
            :disabled="!(oneByOneDisabled && allInDisabled)"
          >
            E
          </button>
        </div>
      </div>
    </div>
    <!-- 护盾显示条（全屏宽） -->
    <div
      style="
        width: 100%;
        height: 24px;
        background-color: #eee;
        position: fixed;
        bottom: 32px;
        left: 0;
        border-top: 1px solid #007bff;
        border-bottom: 1px solid #007bff;
      "
    >
      <div
        :style="{
          width: (shield / maxShield) * 100 + '%',
          height: '100%',
          backgroundColor: '#007bff',
          transition: 'width 0.3s ease',
        }"
      ></div>
      <div
        style="
          position: absolute;
          top: 0;
          left: 50%;
          transform: translateX(-50%);
          font-weight: bold;
          color: white;
          line-height: 24px;
          font-size: 14px;
          text-shadow: 1px 1px 2px black;
        "
      >
        Shield: {{ shield.toLocaleString() }} / {{ maxShield.toLocaleString() }}
      </div>
    </div>

    <!-- 血量显示条 -->
    <div
      style="
        width: 100%;
        height: 30px;
        background-color: #ccc;
        margin-top: 50px;
        position: fixed;
        bottom: 0;
        left: 0;
      "
    >
      <div
        :style="{
          width: hp + '%',
          height: '100%',
          backgroundColor:
            hp > 60 ? '#28a745' : hp > 30 ? '#ffc107' : '#dc3545',
          transition: 'width 0.3s ease',
        }"
      ></div>
      <div
        style="
          position: absolute;
          top: 0;
          left: 50%;
          transform: translateX(-50%);
          font-weight: bold;
          color: white;
          line-height: 30px;
        "
      >
        HP: {{ hp }} / 100
      </div>
    </div>
  </div>
</template>

<script setup>
import "@/assets/css/bootstrap.min.css";
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { postBlog, getBlogById, editBlog } from "@/net";

const router = useRouter();

const rTarget = [1, 6, 1, 5, 1, 1, 2, 3];
const rProgress = ref([]); // 已填入值
const rAvailable = ref(false);
const rLocked = ref(false); // 表示本回合是否已使用过骰子填入R

const totalDamage = ref(0); // 总伤害
const showDamage = ref(false); // 控制是否显示伤害

const hp = ref(100); // 初始血量 100
const shield = ref(50); // 当前护盾值（示例）
const maxShield = ref(10000); // 最大护盾值

const enemyAttackLog = ref(""); // 敌人攻击信息

const wEnabled = ref(false); // W是否可用

const lastSkillUsed = ref(null); // 'Q' | 'W' | null
const lastQDamage = ref(0);
const lastWShield = ref(0);

// R技能全局状态控制
const isDestinyActive = ref(false);
const destinyTurns = ref(0);
const shieldBonusActive = ref(false);

const noAttackTurns = ref(0); // 敌人未受伤的回合数

const BLOG_ID = "27"; // 可写死

// 骰子数值，初始为问号
const dice = ref({
  die1: "?",
  die2: "?",
  die3: "?",
});

// 当前点击次数
let count = ref(0);

// 控制按钮禁用状态
const oneByOneDisabled = ref(false);
const allInDisabled = ref(false);

// 生成 1 到 6 的随机整数
const getRandomDice = () => {
  return isDestinyActive.value ? 6 : Math.floor(Math.random() * 6) + 1;
};

// 在 Q、E 技能中，攻击敌人时重置：
const markEnemyDamaged = () => {
  noAttackTurns.value = 0;
};

// OneByOne 点击事件
const rollOneByOne = () => {
  if (count.value >= 3) return;

  count.value++;
  allInDisabled.value = true;

  if (count.value === 1) {
    dice.value.die1 = getRandomDice();
  } else if (count.value === 2) {
    dice.value.die2 = getRandomDice();
  } else if (count.value === 3) {
    dice.value.die3 = getRandomDice();
    oneByOneDisabled.value = true;
  }

  checkWAvailable();
};

const fillR = (key) => {
  if (rLocked.value) {
    ElMessage({
      message: "本回合已填入一个骰子，不能再填！",
      type: "warning",
    });
    return;
  }

  const val = dice.value[key];
  if (typeof val !== "number") return;

  // 当前数字已填满上限，则不能填
  const targetCount = rTarget.filter((v) => v === val).length;
  const currentCount = rProgress.value.filter((v) => v === val).length;

  if (currentCount >= targetCount) {
    ElMessage({
      message: `不能再填入 ${val}，已达到限制`,
      type: "warning",
    });
    return;
  }

  rProgress.value.push(val);
  dice.value[key] = "?";
  rLocked.value = true; // ✅ 本回合锁定

  // 检查是否完成
  const temp = [...rProgress.value].sort().join();
  const targetSorted = [...rTarget].sort().join();

  if (rProgress.value.length === rTarget.length && temp === targetSorted) {
    rAvailable.value = true;
    ElMessage({
      message: "🎉 R技能解锁（114514牌型达成）！",
      type: "success",
    });
  }
};

// 命运解放
const useSkillR = () => {
  if (!rAvailable.value) return;

  ElMessage({
    message: "🔥 命运解放发动！全能力极限提升 3 回合！",
    type: "success",
    duration: 3000,
  });

  // 状态启动
  isDestinyActive.value = true;
  destinyTurns.value = 4;
  shieldBonusActive.value = true;

  if (hp.value <= 90) {
    hp.value = 100;
    ElMessage({
      message: "💖 命运之力恢复你的生命至满血！",
      type: "info",
    });
  }

  rProgress.value = [];
  rAvailable.value = false;
  resetRound();
};

// AllIn 点击
const rollAllIn = () => {
  dice.value.die1 = getRandomDice();
  dice.value.die2 = getRandomDice();
  dice.value.die3 = getRandomDice();
  oneByOneDisabled.value = true;
  allInDisabled.value = true;

  checkWAvailable();
};

const useSkillQ = () => {
  if (!oneByOneDisabled.value || !allInDisabled.value) return;

  const sum =
    (typeof dice.value.die1 === "number" ? dice.value.die1 : 0) +
    (typeof dice.value.die2 === "number" ? dice.value.die2 : 0) +
    (typeof dice.value.die3 === "number" ? dice.value.die3 : 0);

  const factor = isDestinyActive.value ? 2.5 : 1;
  const damage = sum * 10 * factor;

  lastQDamage.value = damage;
  totalDamage.value += damage;
  showDamage.value = true;

  markEnemyDamaged();

  lastSkillUsed.value = "Q";
  resetRound();
};

// 封装一个 resetRound() 函数，用于所有技能使用后的状态重置 + 敌人攻击：
const resetRound = () => {
  // 命运解放倒计时处理
  if (isDestinyActive.value) {
    // 每回合恢复护盾 60（不超过当前 max）
    const recoverAmount = 60;
    const effectiveMax = shieldBonusActive.value
      ? maxShield.value * 2
      : maxShield.value;

    if (shield.value < effectiveMax) {
      const healed = Math.min(recoverAmount, effectiveMax - shield.value);
      shield.value += healed;
      ElMessage({
        message: `✨ 命运之力保护你，自动恢复 ${healed} 点护盾`,
        type: "info",
      });
    }

    destinyTurns.value--;
    if (destinyTurns.value <= 0) {
      isDestinyActive.value = false;
      shieldBonusActive.value = false;
      ElMessage({
        message: "🕓 命运解放结束，你回归凡人之身。",
        type: "warning",
      });
    }
  }

  enemyAttack();

  // resetRound 末尾添加
  if (shield.value <= 100) {
    noAttackTurns.value = 0; // ✅ 护盾低于等于100时自动清空计数
  } else if (!showDamage.value) {
    noAttackTurns.value++; // ✅ 累计未攻击回合
  } else {
    showDamage.value = false;
  }

  // 🧊 碎盾判定
  if (
    shield.value > 100 &&
    noAttackTurns.value >= 3 &&
    Object.values(dice.value).includes(4)
  ) {
    shield.value = 0;
    ElMessage({
      message: "💥 护盾碎裂！长时间未攻击触发碎盾机制。",
      type: "error",
    });
  }

  dice.value.die1 = "?";
  dice.value.die2 = "?";
  dice.value.die3 = "?";
  count.value = 0;
  oneByOneDisabled.value = false;
  allInDisabled.value = false;
  wEnabled.value = false;
  rLocked.value = false;

  if (hp.value <= 0) {
    ElMessageBox.confirm("你已阵亡，是否要上传本次分数？", "💀 GAME OVER", {
      confirmButtonText: "上传并返回首页",
      cancelButtonText: "直接返回首页",
      type: "warning",
    })
      .then(() => {
        // 询问用户名
        ElMessageBox.prompt(
          "请输入你的名字，用于记录分数排行：",
          "输入用户名",
          {
            confirmButtonText: "确认上传",
            cancelButtonText: "取消上传",
            inputPattern: /^.{1,20}$/, // 限制1-20字符
            inputErrorMessage: "请输入1~20字符的名字",
          }
        )
          .then(({ value }) => {
            // 有效用户名后再上传分数
            submitScore(value).finally(() => {
              router.push("/");
            });
          })
          .catch(() => {
            // 用户取消上传用户名，直接跳首页
            router.push("/");
          });
      })
      .catch(() => {
        // 点击了“直接返回首页”
        router.push("/");
      });

    return;
  }
};

// 上传分数
const submitScore = async (username) => {
  getBlogById(BLOG_ID, (blog) => {
    const newLine = `<p>${username} - ${
      totalDamage.value
    } damage - ${new Date().toLocaleString()}</p>`;

    const updatedContent = (blog.content?.trim() || "") + newLine;

    editBlog(
      BLOG_ID,
      {
        title: blog.title,
        category: blog.category,
        description: blog.description,
        content: updatedContent,
      },
      () => {
        ElMessage.success("🎉 分数已成功上传！");
      }
    );
  });
};

// 回合结束后，敌方攻击
const enemyAttack = () => {
  const validKeys = Object.keys(dice.value).filter(
    (key) => typeof dice.value[key] === "number"
  );

  if (validKeys.length === 0) return;

  const selectedKey = validKeys[Math.floor(Math.random() * validKeys.length)];
  const selectedValue = dice.value[selectedKey];

  // ✅ 容错判断，确保 selectedValue 是数字
  if (typeof selectedValue !== "number") {
    console.warn("敌人攻击跳过：骰子值无效", selectedKey, selectedValue);
    return;
  }

  const damage = selectedValue * 10;

  if (shield.value >= damage) {
    shield.value -= damage;
  } else {
    const remainingDamage = damage - shield.value;
    shield.value = 0;
    hp.value = Math.max(hp.value - remainingDamage, 0); // ✅ 正常扣血
  }

  ElMessage({
    message: `敌人使用 ${selectedKey}（点数 ${selectedValue}）造成了 ${damage} 点伤害！`,
    type: "warning",
    duration: 3000,
  });
};

// 检查是否满足条件（有两个以上相同点数）
const checkWAvailable = () => {
  const counts = {};
  for (const val of Object.values(dice.value)) {
    if (typeof val === "number") {
      counts[val] = (counts[val] || 0) + 1;
    }
  }
  wEnabled.value = Object.values(counts).some((count) => count >= 2);
};

// W技能
const useSkillW = () => {
  if (!wEnabled.value) return;

  // 统计每个数字出现次数
  const counts = {};
  for (const [_, val] of Object.entries(dice.value)) {
    if (typeof val === "number") {
      counts[val] = (counts[val] || 0) + 1;
    }
  }

  // 找到重复的数字和数量
  const match = Object.entries(counts).find(([_, count]) => count >= 2);
  if (!match) return;

  const [numStr, repeatCount] = match;
  const num = Number(numStr);
  const multiplier = repeatCount === 3 ? 3 : 2;
  const addedShield = num * multiplier * 10;

  lastWShield.value = addedShield;
  shield.value += addedShield;

  ElMessage({
    message: `W技能发动！${repeatCount}个${num}点数 → 增加护盾 ${
      num * 10
    } × ${multiplier} = ${addedShield}`,
    type: "success",
    duration: 3000,
  });

  lastSkillUsed.value = "W"; // ✅ 记录
  resetRound(); // ✅ 用统一函数
};

const useSkillE = () => {
  if (!oneByOneDisabled.value || !allInDisabled.value) return;

  if (lastSkillUsed.value === "Q") {
    const damage = shield.value * 10;
    totalDamage.value += damage;

    ElMessage({
      message: `E技能发动！牺牲全部护盾，造成 ${damage} 点爆发伤害🔥`,
      type: "success",
      duration: 3000,
    });

    shield.value = 0; // 清空护盾
  } else if (lastSkillUsed.value === "W") {
    const doubled = Math.min(shield.value + lastWShield.value, maxShield.value);

    ElMessage({
      message: `E技能发动！共鸣W技能，再次获得护盾 +${
        lastWShield.value
      }，当前护盾为 ${doubled.toLocaleString()}🛡`,
      type: "success",
      duration: 3000,
    });

    shield.value = doubled;
  } else {
    ElMessage({
      message: "E技能失败：请先使用 Q 或 W 技能再触发 E。",
      type: "error",
      duration: 3000,
    });
    return;
  }

  lastSkillUsed.value = null;
  resetRound();
};
</script>

<style>
.gold-glow {
  animation: glow 1s infinite alternate;
  box-shadow: 0 0 8px gold, 0 0 16px gold;
  border-color: gold !important;
}

@keyframes glow {
  from {
    box-shadow: 0 0 5px gold;
  }
  to {
    box-shadow: 0 0 20px gold;
  }
}
</style>
