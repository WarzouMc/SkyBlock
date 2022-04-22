# File extension 
    .island

# Format

## File structure

<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  overflow:hidden;padding:10px 5px;word-break:normal;}
.tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}
.tg .tg-9wq8{border-color:inherit;text-align:center;vertical-align:middle}
.tg .tg-uzvj{border-color:inherit;font-weight:bold;text-align:center;vertical-align:middle}
.tg .tg-vrnj{border-color:inherit;font-style:italic;text-align:center;vertical-align:middle}
.tg .tg-nrix{border-color:inherit;text-align:center;vertical-align:middle}
</style>
<table class="tg">
<thead>
  <tr>
    <th class="tg-uzvj">Name</th>
    <th class="tg-uzvj" colspan="3">Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td class="tg-9wq8">signature</td>
    <td class="tg-9wq8" colspan="3">u4</td>
  </tr>
  <tr>
    <td class="tg-9wq8" rowspan="3">minecraft_version</td>
    <td class="tg-9wq8" rowspan="3">u3</td>
    <td class="tg-vrnj">major</td>
    <td class="tg-9wq8">u1</td>
  </tr>
  <tr>
    <td class="tg-vrnj">minor</td>
    <td class="tg-9wq8">u1</td>
  </tr>
  <tr>
    <td class="tg-vrnj">revision</td>
    <td class="tg-9wq8">u1</td>
  </tr>
  <tr>
    <td class="tg-nrix">used_blocks_count</td>
    <td class="tg-nrix" colspan="3">u2</td>
  </tr>
  <tr>
    <td class="tg-nrix">used_blocks</td>
    <td class="tg-nrix" colspan="3"><span style="font-style:italic">block</span>[used_blocks_count]</td>
  </tr>
  <tr>
    <td class="tg-9wq8" rowspan="3">cuboid_size</td>
    <td class="tg-9wq8" rowspan="3">u6</td>
    <td class="tg-vrnj">width</td>
    <td class="tg-9wq8">u2</td>
  </tr>
  <tr>
    <td class="tg-vrnj">length</td>
    <td class="tg-9wq8">u2</td>
  </tr>
  <tr>
    <td class="tg-vrnj">height</td>
    <td class="tg-9wq8">u2</td>
  </tr>
  <tr>
    <td class="tg-9wq8">cuboid</td>
    <td class="tg-9wq8" colspan="3"><span style="font-style:italic">varint</span>[width*length*height]</td>
  </tr>
</tbody>
</table>

## Block structure

