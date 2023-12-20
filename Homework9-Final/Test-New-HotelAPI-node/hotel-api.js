const axios = require('axios');

async function getUniqueRoomCount()
{
    const options = {
    method: 'GET',
    url: 'https://booking-com.p.rapidapi.com/v1/hotels/room-list',
    params: {
        currency: 'USD',
        adults_number_by_rooms: '1',
        units: 'metric',
        checkin_date: '2024-02-12',
        hotel_id: '406833',
        locale: 'en-us',
        checkout_date: '2024-02-13'
    },
    headers: {
        'X-RapidAPI-Key': '054999d8c2mshb298a4b5c4ddea0p15eb49jsnabf9c5983234',
        'X-RapidAPI-Host': 'booking-com.p.rapidapi.com'
    }
    };

    try {
        const response = await axios.request(options);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}
getUniqueRoomCount()