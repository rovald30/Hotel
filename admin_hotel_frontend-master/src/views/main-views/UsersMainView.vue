<script setup>
import { usePersonStore } from "@/stores/person";
import { storeToRefs } from "pinia";
import BasicTable from "@/components/_atoms/BasicTable/BasicTable.vue";
import BaseSelector from "@/components/_atoms/BaseSelector/BaseSelector.vue";
import { ref, computed, onBeforeMount } from "vue";
import Pagination from "@/components/_atoms/Pagination/Pagination.vue";

const personStore = usePersonStore();

const { responseData } = storeToRefs(usePersonStore());
const page = ref(1);
const currentPage = ref(1);
const hasMorePages = ref(true);
const filterBy = ref(null);
const filterValue = ref(null);

const peopleData = computed(() => responseData.value?.people);

const totalEntities = computed(() => {
  return personStore.responseData
    ? personStore.responseData.totalPeopleLength
    : null;
});
let totalPages = computed(() => {
  return totalEntities.value
    ? Math.ceil(totalEntities.value / perPage.value)
    : null;
});

const tableHeaders = [
  "Identity code",
  "Firstname",
  "Lastname",
  "Date of birth",
  "Country",
  "Phone number",
];

const pageSizeOptions = ref(["10", "25", "50", "100"]);

const perPage = ref(pageSizeOptions.value[0] * 1);


const getAllPersonsWithPageSizeParam = async () => {
  await personStore.getAllPersonsWithParams({
    pageSize: perPage.value,
  });
};

const getAllPersonsWithPageSizeAndFilter = async () => {
  await personStore.getAllPersonsWithParams({
    pageNumber: currentPage.value - 1,
    pageSize: perPage.value,
    filterBy: filterBy.value,
    filterValue: filterValue.value,
  });
};

onBeforeMount(async () => {
  await getAllPersonsWithPageSizeParam();
  perPage.value = pageSizeOptions.value[0] * 1;
});

const changePageSizeOptions = (event) => {
  perPage.value = event.target.value;
  if (filterBy.value === "" || filterValue.value === "") {
    getAllPersonsWithPageSizeParam();
  }
  onChange([filterBy.value, filterValue.value]);
  perPage.value = event.target.value;
  currentPage.value = 1;
};

const changePage = (pageNumber) => {
  page.value = pageNumber;
  currentPage.value = pageNumber;
  getAllPersonsWithPageSizeAndFilter();
};

const onChange = async (properties) => {
  currentPage.value = 1;
  filterBy.value = properties[0];
  filterValue.value = properties[1];

  if (filterValue.value !== "") {
    await getAllPersonsWithPageSizeAndFilter();
  } else {
    await getAllPersonsWithPageSizeParam();
  }
};

const orderType = ref("ASC");
const orderBy = async (propertyKey) => {
  orderType.value === "ASC"
    ? (orderType.value = "DESC")
    : (orderType.value = "ASC");
  await personStore.getAllPersonsWithParams({
    orderBy: propertyKey,
    orderType: orderType.value,
    filterBy: filterBy.value,
    filterValue: filterValue.value,
    pageNumber: currentPage.value - 1,
    pageSize: perPage.value,
  });
};
</script>

<template>
  <!-- This part is only if responseData return null, it is mean, that something is broken. -->
  <!-- Because if is all work and no data in DB, then should return Array with zero length. -->
  <div v-if="peopleData === null || peopleData === undefined">
    No user data to show, maybe something is broken...
  </div>
  <div v-else class="w-screen p-20">
    <BaseSelector
      class="w-20 m-20 -mb-16"
      label-text="Page size"
      :options="pageSizeOptions"
      @change="changePageSizeOptions($event)"
    />

    <BasicTable
      :show-indexes="true"
      :tableData="peopleData"
      :table-headers="tableHeaders"
      :current-page="currentPage"
      :items-per-page="perPage * 1"
      :editable="true"
      :removable="true"
      @search="onChange"
      @orderBy="orderBy"
    />
    <div v-if="peopleData && peopleData.length > 0">
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

<style scoped></style>
