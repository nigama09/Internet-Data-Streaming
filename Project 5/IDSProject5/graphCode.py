import numpy as np
import matplotlib as plt
from matplotlib.backends.backend_pdf import PdfPages

import matplotlib.pyplot as plt
  
actual = []
estimated = []
  
f = open('/content/PlotPoints.txt','r')
for row in f:
    row = row.split(' ')
    actual.append(int(row[0]))
    estimated.append(int(row[1]))



# making a plot
opt = PdfPages('output1Graph.pdf')

plt.xlabel('actual spread')
plt.ylabel('estimated spread')


ax=plt.gca()
ax.set_xlim([0,500])
ax.set_ylim([0,700])
plt.scatter(actual, estimated, marker="+")
plt.plot([0,500], [0,500], "r-")



opt.savefig()
plt.show()

opt.close()
