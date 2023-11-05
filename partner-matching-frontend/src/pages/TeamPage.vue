<script setup lang="ts">

import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {showFailToast} from "vant";

const router = useRouter();

const teamList = ref([]);
const doJoinTeam = () =>{
  router.push({
    path: "team/add"
  })
}

onMounted(async () => {
  const res = await myAxios.get("/team/list");
  if (res?.code === 0){
    teamList.value = res.data;
  } else {
    showFailToast("加载失败，请刷新重试")
  }
})
</script>

<template>
  <div id="teamPage">
    <van-button type="primary" @click="doJoinTeam">加入队伍</van-button>
    <team-card-list :teamList="teamList"/>
  </div>
</template>


<style scoped>

</style>