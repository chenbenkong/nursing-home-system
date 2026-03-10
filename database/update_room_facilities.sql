-- 更新房间设施，删除不存在于前端选项的设施
UPDATE room SET facilities = '空调、电视、独立卫生间' WHERE id = 1;
UPDATE room SET facilities = '空调、电视、独立卫生间' WHERE id = 2;
UPDATE room SET facilities = '空调、电视、独立卫生间' WHERE id = 3;
UPDATE room SET facilities = '空调、电视、独立卫生间' WHERE id = 4;
UPDATE room SET facilities = '空调、电视、独立卫生间、阳台' WHERE id = 5;
