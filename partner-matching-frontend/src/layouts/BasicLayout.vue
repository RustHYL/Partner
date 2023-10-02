<template>
  <van-nav-bar
      title="标题"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
      fixed
  >
    <template #right>
      <van-icon name="search" size="18" />
    </template>
  </van-nav-bar>

  <div id="content">
    <template v-if="active === 'index'">
      <Index />
    </template>
    <template v-if="active === 'team'">
      <Team />
    </template>
  </div>

  <van-tabbar v-model="active" @change="onChange" fixed>
    <van-tabbar-item icon="home-o" name="index">主页</van-tabbar-item>
    <van-tabbar-item icon="search" name="team">队伍页</van-tabbar-item>
    <van-tabbar-item icon="friends-o" name="user">个人</van-tabbar-item>
  </van-tabbar>
</template>

<script setup lang="ts">
import {showToast} from "vant";
import {ref} from "vue";
import Index from "../pages/Index.vue"
import Team from "../pages/Team.vue";

const onClickLeft = () => alert("左")
const onClickRight = () => alert("右");
const active = ref("index");
const onChange = (index:any) => showToast(`标签 ${index}`);
</script>

<style>

</style>