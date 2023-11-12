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

const listTeam = async (val = '', status = 0) => {
  const res = await myAxios.get("/team/list", {
    params: {
      searchText: val,
      pageNum: 1,
      status,
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

const active = ref('public');

const onTabChange = (name) => {
  if (name === 'public'){
    listTeam(searchText.value, 0);
  }else {
    listTeam(searchText.value, 2);
  }
}

onMounted(() => {
  listTeam();
})
</script>

<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch"/>
    <van-tabs v-model:active="active" @change="onTabChange">
      <van-tab title="公开" name="public"/>
      <van-tab title="加密" name="private"/>
    </van-tabs>
    <div style="margin-bottom: 12px"/>
    <team-card-list :teamList="teamList" />
    <van-empty v-if="!teamList || teamList.length < 1" description="数据为空" />
    <van-button type="primary" icon="plus" style="position: fixed; bottom: 60px; right: 12px; height: 50px; width: 50px; border-radius: 50%" @click="doCreateTeam"/>
  </div>
</template>


<style scoped>

</style>