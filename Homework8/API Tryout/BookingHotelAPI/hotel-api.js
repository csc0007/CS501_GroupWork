const checkin = '2023-12-06';

// Create a Date object from the checkin string
const checkinDate = new Date(checkin);

// Add six months
checkinDate.setMonth(checkinDate.getMonth() + 6);

// Format the date back into a string
const checkinPlusSixMonths = checkinDate.toISOString().split('T')[0];

console.log('Original check-in date:', checkin);
console.log('Check-in date plus six months:', checkinPlusSixMonths);