// Generated by CoffeeScript 1.9.2
'use strict';
angular.module('ZooLib.directives.usageTable', []).directive('usageTable', function(itemService, influxDb, $q) {
  return {
    restrict: 'E',
    replace: true,
    templateUrl: 'partials/directives/usageTable.html',
    link: function(scope, elem, attr) {
      var activePowerItems, addZ, consumptionItems, queries, queryDiff, rearrangeConsumptionData;
      consumptionItems = $q.defer();
      itemService.getByName({
        itemName: 'gConsumptions'
      }, function(data) {
        return consumptionItems.resolve({
          globalConsumption: data.state,
          rooms: data.members
        });
      });
      activePowerItems = $q.defer();
      itemService.getByName({
        itemName: 'gPower'
      }, function(data) {
        return activePowerItems.resolve({
          globalPower: data.state,
          rooms: data.members
        });
      });
      addZ = function(n) {
        if (n < 10) {
          return '0' + n;
        } else {
          return '' + n;
        }
      };
      queryDiff = function(minusDay, minusMonth) {
        var d, date, m, y;
        if (minusDay == null) {
          minusDay = 0;
        }
        if (minusMonth == null) {
          minusMonth = 0;
        }
        date = new Date();
        date.setHours(0, 0, 0, 0);
        date.setDate(date.getDate() - minusDay);
        date.setMonth(date.getMonth() - minusMonth);
        y = date.getFullYear();
        m = addZ(date.getMonth() + 1);
        d = addZ(date.getDate());
        return influxDb.query({
          query: "select difference(value) as diff from /^consum/i where time > '" + y + "-" + m + "-" + d + "'"
        }, function(data) {
          return console.log("Influx response to time " + date + " arrived. " + (data != null ? data.length : void 0) + " rows.");
        });
      };
      queries = $q.all({
        day: queryDiff(0).$promise,
        week: queryDiff(7).$promise,
        month: queryDiff(0, 1).$promise,
        items: consumptionItems.promise,
        power: activePowerItems.promise
      });
      queries.then(function(data) {
        var lut;
        console.log(data);
        scope.data = data.items;
        lut = rearrangeConsumptionData(data);
        return data.items.rooms.forEach(function(room) {
          var ref;
          room.currentPower = 0;
          room.consumptionDay = room.consumptionWeek = room.consumptionMonth = 0;
          return (ref = room.members) != null ? ref.forEach(function(roomMember) {
            room.consumptionDay += Math.abs(lut.day[roomMember.name]);
            room.consumptionWeek += Math.abs(lut.week[roomMember.name] + room.consumptionDay);
            room.consumptionMonth += Math.abs(lut.month[roomMember.name] + room.consumptionWeek);
            return room.cost = room.consumptionMonth * .3;
          }) : void 0;
        });
      });
      return rearrangeConsumptionData = function(data) {
        var day, month, week;
        day = {};
        week = {};
        month = {};
        data.day.forEach(function(entry) {
          return day[entry.name] = entry.points[0][1];
        });
        data.week.forEach(function(entry) {
          return week[entry.name] = entry.points[0][1];
        });
        data.month.forEach(function(entry) {
          return month[entry.name] = entry.points[0][1];
        });
        return {
          day: day,
          week: week,
          month: month
        };
      };
    }
  };
});
