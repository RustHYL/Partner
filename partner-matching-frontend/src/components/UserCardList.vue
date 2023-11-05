<script setup lang="ts">
import {UserType} from "../models/user";

interface UserCardListProps {
  userList: UserType[];
}
  const props = withDefaults(defineProps<UserCardListProps>(),{
    userList: [] as UserType[]
  })
</script>

<template>
  <van-card
      v-for="user in props.userList"
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
</template>

<style scoped>

</style>