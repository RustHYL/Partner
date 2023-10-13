<template>
  <van-card
      v-for="user in userList"
      :desc="user.profile"
      :title="`${user.username}\u3000\u3000 NO.${user.verifyCode}`"
      :thumb="user.avatarUrl"
      style="text-align: start"
  >
    <template #tags>
      <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">{{ tag }}</van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>
  <van-empty v-if="!userList || userList.length < 1" description="暂无数据" />
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios.ts"
import qs from 'qs';

const route = useRoute();

const {tags} = route.query;

const userList = ref([]);

onMounted(async () => {
  const userListData = await myAxios.get('/user/recommend', {
    params: {},
  })
      .then(function (response) {
        console.log('/user/recommend success', response);
        return response.data;
      })
      .catch(function (error) {
        console.error('/user/recommend error', error);
      })
  if (userListData){
    userListData.forEach(user => {
      if (user.tags){
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
})

</script>

<style scoped>

</style>
