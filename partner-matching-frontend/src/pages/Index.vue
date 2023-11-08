<template>
  <van-cell center title="智能匹配模式">
    <template #right-icon>
      <van-switch v-model="isMatchMode" />
    </template>
  </van-cell>
  <user-card-list :user-list="userList" :loading="loading"/>
  <van-empty v-if="!userList || userList.length < 1" description="数据为空" />
</template>

<script setup lang="ts">
import { ref, watchEffect} from 'vue';
import myAxios from "../plugins/myAxios.ts"
import UserCardList from "../components/UserCardList.vue";
import {UserType} from "../models/user";

const isMatchMode =ref<boolean>(false);

const userList = ref([]);

const loading = ref<boolean>(true);

const loadData = async () =>{
  let userListData = [];
  loading.value = true;
  //智能模式，匹配用户
  if (isMatchMode.value){
    const num = 10;
    userListData = await myAxios.get('/user/match', {
      params:{
        num,
      }
    })
        .then(function (response) {
          console.log('/user/match success', response);
          return response?.data;
        })
        .catch(function (error) {
          console.error('/user/match error', error);
        })
  } else {
    //普通模式分页查询
    userListData = await myAxios.get('/user/recommend', {
      params: {
        pageNum:1,
        pageSize:8
      },
    })
        .then(function (response) {
          console.log('/user/recommend success', response);
          return response?.data?.records;
        })
        .catch(function (error) {
          console.error('/user/recommend error', error);
        })
  }

  if (userListData){
    userListData.forEach((user:UserType) => {
      if (user.tags){
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
  loading.value = false;
}


watchEffect(() => {
  loadData();
})


</script>

<style scoped>

</style>
