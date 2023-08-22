import api from "../api";

const getAllPersons = () => {
  return api().get("/api/person");
};

const getPersonByIdentityCode = (identityCode) => {
  return api().get(`/api/person/public/${identityCode}`);
};

const addPerson = (
  identityCode,
  firstName,
  lastName,
  dateOfBirth,
  country,
  phoneNumber
) => {
  return api().post(`/api/person/public/`, {
    identityCode: identityCode,
    firstName: firstName,
    lastName: lastName,
    dateOfBirth: dateOfBirth,
    country: country,
    phoneNumber: phoneNumber,
  });
};

export { getAllPersons, getPersonByIdentityCode, addPerson };
