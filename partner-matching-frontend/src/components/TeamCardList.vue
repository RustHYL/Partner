<script setup lang="ts">
import {TeamType} from "../models/team";

interface TeamCardListProps {
  teamList: TeamType[];
}
  const props = withDefaults(defineProps<TeamCardListProps>(),{
    teamList: [] as TeamType[]
  })
</script>

<template>
  <van-card
      v-for="team in props.teamList"
      :desc="team.description"
      :title="`${team.name}`"
      :price="'最大人数: ' + team.maxNum"
      style="text-align: start"
  >
    <template #tags>
      <van-tag plain type="primary" style="margin-right: 8px; margin-top: 8px">{{ team.status }}</van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>
</template>

<style scoped>

</style>