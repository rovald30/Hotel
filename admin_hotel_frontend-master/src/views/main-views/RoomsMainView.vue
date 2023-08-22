<script setup>
import { useRoomsStore } from "@/stores/rooms.js";
import { computed, onBeforeMount, ref } from "vue";
import { storeToRefs } from "pinia";
import BasicTable from "@/components/_atoms/BasicTable/BasicTable.vue";
import Pagination from "@/components/_atoms/Pagination/Pagination.vue";
import BaseSelector from "@/components/_atoms/BaseSelector/BaseSelector.vue";

const roomsStore = useRoomsStore();
const { responseData } = storeToRefs(useRoomsStore());

const tableHeaders = [
  "id",
  "m2",
  "Floor",
  "Room Number",
  "Beds",
  "Type",
  "Available",
];

const page = ref(1);
const currentPage = ref(1);
const hasMorePages = ref(true);
const filterBy = ref(null);
const filterValue = ref(null);

const roomsData = computed(() => responseData.value?.rooms);

const totalEntities = computed(() => {
  return roomsStore.responseData
    ? roomsStore.responseData.totalRoomsLength
    : null;
});
let totalPages = computed(() => {
  return totalEntities.value
    ? Math.ceil(totalEntities.value / perPage.value)
    : null;
});

const pageSizeOptions = ref(["10", "25", "50", "100"]);
const perPage = ref(pageSizeOptions.value[0] * 1);

const getAllRoomsWithPageSizeParam = async () => {
  await roomsStore.getAllRoomsWithParams({
    pageSize: perPage.value,
  });
};

const getAllRoomsWithPageSizeAndFilter = async () => {
  await roomsStore.getAllRoomsWithParams({
    pageNumber: currentPage.value - 1,
    pageSize: perPage.value,
    filterBy: filterBy.value,
    filterValue: filterValue.value,
  });
};

onBeforeMount(async () => {
  await getAllRoomsWithPageSizeParam();
  perPage.value = pageSizeOptions.value[0] * 1;
});

const changePageSizeOptions = (event) => {
  perPage.value = event.target.value;
  if (filterBy.value === "" || filterValue.value === "") {
    getAllRoomsWithPageSizeParam();
  }
  onChange([filterBy.value, filterValue.value]);
  perPage.value = event.target.value;
  currentPage.value = 1;
};

const changePage = (pageNumber) => {
  page.value = pageNumber;
  currentPage.value = pageNumber;
  getAllRoomsWithPageSizeAndFilter();
};

const onChange = async (properties) => {
  currentPage.value = 1;
  filterBy.value = properties[0];
  filterValue.value = properties[1];

  if (filterValue.value !== "") {
    await getAllRoomsWithPageSizeAndFilter();
  } else {
    await getAllRoomsWithPageSizeParam();
  }
};

const orderType = ref("ASC");
const orderBy = async (propertyKey) => {
  orderType.value === "ASC"
    ? (orderType.value = "DESC")
    : (orderType.value = "ASC");
  await roomsStore.getAllRoomsWithParams({
    orderBy: propertyKey,
    orderType: orderType.value,
    filterBy: filterBy.value,
    filterValue: filterValue.value,
    pageNumber: currentPage.value - 1,
    pageSize: perPage.value,
  });
};

const onRemove = async (roomId) => {
  await roomsStore.deleteRoom(roomId);
};
const onUpdate = async (room) => {
  await roomsStore.updateRoomInfo(room);
};

const roomType = (key, value) => {
  if (value === "") {
    getAllRoomsWithPageSizeParam();
  } else {
    filterBy.value = key;
    filterValue.value = value;
    getAllRoomsWithPageSizeAndFilter();
  }
};
</script>

<template>
  <div v-if="roomsData === null || roomsData === undefined">
    Here will show table with rooms..
  </div>
  <div v-else>
    <BaseSelector
      class="w-20 m-20 -mb-16"
      label-text="Page size"
      :options="pageSizeOptions"
      @change="changePageSizeOptions($event)"
    />

    <BasicTable
      :show-indexes="false"
      :table-data="roomsData"
      :table-headers="tableHeaders"
      :removable="true"
      :editable="true"
      :current-page="currentPage"
      :items-per-page="perPage * 1"
      :types="['', 'ECONOMY', 'REGULAR', 'KING_SIZE', 'DELUXE']"
      :boolean-selector-for="['Available']"
      :not-allow-to-change="['id', 'floorId', 'roomNumber']"
      @removeEntity="onRemove"
      @updateEntity="onUpdate"
      @search="onChange"
      @orderBy="orderBy"
      @updateType="roomType"
    />
    <div v-if="totalEntities && totalEntities > 0">
      <Pagination
        :total-pages="totalPages"
        :total="totalEntities"
        :per-page="perPage * 1"
        :current-page="currentPage"
        :has-more-pages="hasMorePages"
        @pagechanged="changePage"
      />
    </div>
  </div>
</template>

<style></style>
