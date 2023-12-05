const axios = require('axios');
const checkin='2023-12-06';
const checkout='2023-12-07';

async function getUniqueRoomCount(checkin, checkout)
{
    const options = {
    method: 'GET',
    url: 'https://booking-com-api3.p.rapidapi.com/booking/blockAvailability',
    params: {
        is_24hr: '0',
        show_test: '0',
        checkin: checkin,
        show_only_test: '0',
        units: 'metric',
        allow_past: '0',
        user_platform: 'desktop',
        guest_qty: '1',
        checkout: checkout,
        language: 'en',
        hotel_ids: '191981',
        https_photos: '0',
        detail_level: '0',
        guest_cc: 'us'
    },
    headers: {
        Accept: 'application/json',
        'X-RapidAPI-Key': 'PUT-API-KEY-HERE',
        'X-RapidAPI-Host': 'booking-com-api3.p.rapidapi.com'
    }
    };


    try {
        const response = await axios.request(options);
        const blocks = response.data.result.map(item => item.block).flat();
        const uniqueRoomIds = new Set(blocks.map(block => block.room_id));
        return uniqueRoomIds.size;
    } catch (error) {
        console.error(error);
        return 0; // Return 0 in case of an error
    }
}


async function compareRoomAvailability(originalCheckin, originalCheckout) {
    const checkin6month = new Date(originalCheckin);
    checkin6month.setMonth(checkin6month.getMonth() + 6);
    const checkin6monthSTR = checkin6month.toISOString().split('T')[0];
    const checkout6month = new Date(originalCheckout);
    checkout6month.setMonth(checkout6month.getMonth() + 6);
    const checkout6monthSTR = checkout6month.toISOString().split('T')[0];
     
    // Original dates
    const originalCount = await getUniqueRoomCount(originalCheckin, originalCheckout);
    const sixMonthsLaterCount = await getUniqueRoomCount(checkin6monthSTR, checkout6monthSTR);

    console.log(`available rooms: ${originalCount}`);
    console.log(`available rooms after six months: ${sixMonthsLaterCount}`);
    // Calculate percentage
    const percentage = (sixMonthsLaterCount - originalCount) / sixMonthsLaterCount * 100;
    console.log(`Percentage of rooms booked: ${percentage.toFixed(2)}%`);
}

// Call the function to get snow surface conditions
compareRoomAvailability(checkin, checkout);
