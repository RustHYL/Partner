<template>
  <div style="text-align: start">
    <van-cell title="昵称" is-link :value="user.username" @click="toEdit('username',user.username)" />
    <van-cell title="用户账号" :value="user.userAccount" />
    <van-cell title="头像" is-link to="user/edit">
      <img style="height: 48px; width: 48px" :src="user.avatarUrl" />
    </van-cell>
    <van-cell title="性别" is-link to="user/edit" :value="user.gender" />
    <van-cell title="电话" is-link to="user/edit" :value="user.phone" />
    <van-cell title="邮箱" is-link to="user/edit" :value="user.email" />
    <van-cell title="校验编号" :value="user.verifyCode" />
    <van-cell title="注册时间" :value="user.createTime.toISOString()" />
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";

const user = {
  id: 1,
  username: 'hapi',
  userAccount: '1257635375',
  avatarUrl: 'https://img1.baidu.com/it/u=3605832793,3252065221&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=1422',
  gender: 0,
  phone: 17815700343,
  email: '1257635375@qq.com',
  userRole: 1,
  verifyCode: '66645',
  createTime: new Date(),
}

const router = useRouter();
const toEdit = (editKey: string, currentValue: string) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      currentValue,
    }
  })
}

</script>

<style scoped>

</style>