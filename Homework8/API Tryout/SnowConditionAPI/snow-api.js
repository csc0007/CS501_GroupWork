const axios = require('axios');

async function getSnowSurfaceConditions(resortName) {
    const axios = require('axios');

    const options = {
      method: 'GET',
      url: `https://ski-resort-forecast.p.rapidapi.com/${resortName}/snowConditions`,
      params: {units: 'i'},
      headers: {
        'X-RapidAPI-Key': '054999d8c2mshb298a4b5c4ddea0p15eb49jsnabf9c5983234',
        'X-RapidAPI-Host': 'ski-resort-forecast.p.rapidapi.com'
      }
    };
    
    try {
        const response = await axios.request(options);
        const snowData = response.data;
        console.log({
            topSnowDepth: snowData.topSnowDepth,
            botSnowDepth: snowData.botSnowDepth,
            freshSnowfall: snowData.freshSnowfall,
            lastSnowfallDate: snowData.lastSnowfallDate
        });
    } catch (error) {
        console.error(error);
    }
}

// Call the function to get snow surface conditions
getSnowSurfaceConditions('Stowe');


