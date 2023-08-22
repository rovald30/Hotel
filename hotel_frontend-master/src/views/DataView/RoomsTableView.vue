<script setup>
import Table from "@/components/_atoms/Table/Table.vue";
import { useRoomsStore } from "@/stores/rooms";
import { storeToRefs } from "pinia";
import DataMainView from "@/views/DataView/DataMainView.vue";

const { responseData } = storeToRefs(useRoomsStore());
</script>

<template>
  <DataMainView />
  <div class="container">
    <Table :table-data="responseData.data" />
  </div>
</template>

<style scoped>
.container {
  padding-top: 10%;
  padding-left: 25%;
}
</style>
