<script setup lang="ts">

import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {showFailToast} from "vant";

const router = useRouter();

const teamList = ref([]);

const searchText = ref('');
const doCreateTeam = () =>{
  router.push({
    path: "team/add"
  })
}

const listTeam = async (val = '') => {
  const res = await myAxios.get("/team/list/create", {
    params: {
      searchText: val,
      pageNum: 1,
    }
  });
  if (res?.code === 0) {
    teamList.value = res.data;
  } else {
    showFailToast("加载失败，请刷新重试")
  }
}
const onSearch = (val) => {
  listTeam(val);
};

onMounted(() => {
  listTeam();
})
</script>

<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch"/>
    <van-button type="primary" @click="doCreateTeam">创建队伍</van-button>
    <team-card-list :teamList="teamList"/>
    <van-empty v-if="!teamList || teamList.length < 1" description="数据为空" />
  </div>
</template>


<style scoped>

</style>